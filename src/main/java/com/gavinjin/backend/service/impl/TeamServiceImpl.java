package com.gavinjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.backend.common.StatusCode;
import com.gavinjin.backend.exception.BusinessException;
import com.gavinjin.backend.model.domain.Team;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.model.domain.UserTeam;
import com.gavinjin.backend.model.enums.TeamStatusEnum;
import com.gavinjin.backend.service.TeamService;
import com.gavinjin.backend.mapper.TeamMapper;
import com.gavinjin.backend.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
* @author gavin
* @description 针对表【team(Team table)】的数据库操作Service实现
* @createDate 2024-03-04 23:18:13
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private UserTeamService userTeamService;

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
}




