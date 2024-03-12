package com.gavinjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.backend.common.StatusCode;
import com.gavinjin.backend.exception.BusinessException;
import com.gavinjin.backend.mapper.TeamMapper;
import com.gavinjin.backend.model.domain.Team;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.model.domain.UserTeam;
import com.gavinjin.backend.model.dto.TeamQuery;
import com.gavinjin.backend.model.enums.TeamStatusEnum;
import com.gavinjin.backend.model.request.TeamJoinRequest;
import com.gavinjin.backend.model.request.TeamQuitRequest;
import com.gavinjin.backend.model.request.TeamUpdateRequest;
import com.gavinjin.backend.model.vo.TeamUserVO;
import com.gavinjin.backend.model.vo.UserVO;
import com.gavinjin.backend.service.TeamService;
import com.gavinjin.backend.service.UserService;
import com.gavinjin.backend.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
* @author gavin
* @description 针对表【team(Team table)】的数据库操作Service实现
* @createDate 2024-03-04 23:18:13
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createTeam(Team team, User loginUser) {
        // Check if the request is empty
        if (team == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // Check if the user has logged in
        if (loginUser == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        final long userId = loginUser.getId();

        // Check team size
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Team size is invalid");
        }
        // Check team name
        String name = team.getName();
        if (StringUtils.isAnyBlank(name) || name.length() > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Team name is invalid");
        }
        // Check description
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Team description is too long");
        }
        // Check the status
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Team status is invalid");
        }
        // Check password
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isNotBlank(password) && password.length() > 32) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "Team password is invalid");
            }
        }
        // Check expiration date
        Date expiration = team.getExpiration();
        if (new Date().after(expiration)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Expiration date is invalid");
        }
        // Check number of teams the user has created
        // TODO: bug here! User can create 100 teams at the same time
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long count = count(queryWrapper);
        if (count >= 5) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "User can create at most 5 teams");
        }

        /* Need transaction to ensure the following 2 operations are executed together */
        // Save team
        team.setId(null);
        team.setUserid(userId);
        boolean saved = save(team);
        Long teamId = team.getId();
        if (!saved || teamId == null) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Fail to save team");
        }

        // Save user-team relation
        UserTeam userTeam = new UserTeam();
        userTeam.setUserid(userId);
        userTeam.setTeamid(teamId);
        userTeam.setJoinedTime(new Date());
        saved = userTeamService.save(userTeam);
        if (!saved) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Fail to save user-team relation");
        }

        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("max_num", maxNum);
            }
            Long userid = teamQuery.getUserid();
            if (userid != null && userid > 0) {
                queryWrapper.eq("userId", userid);
            }
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && !statusEnum.equals(TeamStatusEnum.PUBLIC)) {
                throw new BusinessException(StatusCode.NO_AUTH);
            }
            queryWrapper.eq("status", statusEnum.getValue());
        }
        // Do not show the expired teams
        queryWrapper.and(qw -> qw.isNull("expiration").or().gt("expiration", new Date()));

        List<Team> teamList = list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();

        // Find the team leader
        for (Team team : teamList) {
            Long userid = team.getUserid();
            if (userid == null) {
                continue;
            }
            User user = userService.getById(userid);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setTeamCreator(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }

        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id == null || id < 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Team oldTeam = getById(id);
        if (oldTeam == null) {
            throw new BusinessException(StatusCode.NULL_ERROR, "Can not find the team");
        }

        // Only admin or team creator can edit team information
        if (!Objects.equals(oldTeam.getUserid(), loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(StatusCode.NO_AUTH);
        }
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (TeamStatusEnum.SECRET.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "Secret team must have password");
            }
        }

        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
        return updateById(updateTeam);
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        Long teamId = teamJoinRequest.getTeamId();
        if (teamId == null || teamId < 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Team team = getById(teamId);
        if (team == null) {
            throw new BusinessException(StatusCode.NULL_ERROR, "Team does not exist");
        }
        Date expiration = team.getExpiration();
        if (expiration != null && new Date().after(expiration)) {
            throw new BusinessException(StatusCode.NULL_ERROR, "Team has expired");
        }
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum.equals(TeamStatusEnum.PRIVATE)) {
            throw new BusinessException(StatusCode.NULL_ERROR, "Private team cannot be joined");
        }
        String password = teamJoinRequest.getPassword();
        if (statusEnum.equals(TeamStatusEnum.SECRET)) {
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())) {
                throw new BusinessException(StatusCode.NULL_ERROR, "Invalid password");
            }
        }

        // One user can join at most 5 teams at a time
        long userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", userId);
        long hasJoinedNum = userTeamService.count(userTeamQueryWrapper);
        if (hasJoinedNum >= 5) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Can join at most 5 teams");
        }

        // Can not join a team twice
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", userId);
        userTeamQueryWrapper.eq("teamId", teamId);
        hasJoinedNum = userTeamService.count(userTeamQueryWrapper);
        if (hasJoinedNum > 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Current user has already joined the team");
        }

        // Check if the team has already been filled
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", teamId);
        hasJoinedNum = userTeamService.count(userTeamQueryWrapper);
        if (hasJoinedNum >= team.getMaxNum()) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Team is full");
        }

        // Create the user-team relation
        UserTeam userTeam = new UserTeam();
        userTeam.setUserid(userId);
        userTeam.setTeamid(teamId);
        userTeam.setJoinedTime(new Date());
        return userTeamService.save(userTeam);
    }

    @Override
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        Long teamId = teamQuitRequest.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(StatusCode.NULL_ERROR);
        }

        long userId = loginUser.getId();
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamid(teamId);
        userTeam.setUserid(userId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(userTeam);
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Didn't enter the team");
        }

        // if the team has only 1 person, delete the team
        long teamHasJoinNum = countTeamUserByTeamId(teamId);
        if (teamHasJoinNum == 1) {
            removeById(teamId);
        } else {
            // check if is leader
            if (team.getUserid() == userId) {
                // give out the leader to another people in the team
                QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                queryWrapper.eq("teamId", teamId);
                queryWrapper.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() < 2) {
                    throw new BusinessException(StatusCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserid();

                // Update the new leader
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserid(nextTeamLeaderId);
                boolean result = updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(StatusCode.SYSTEM_ERROR, "Fail to update leader");
                }
            }
        }
        return userTeamService.remove(queryWrapper);
    }

    private long countTeamUserByTeamId(long teamId) {
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        return userTeamService.count(queryWrapper);
    }
}