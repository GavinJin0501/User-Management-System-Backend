package com.gavinjin.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavinjin.backend.common.BaseResponse;
import com.gavinjin.backend.common.ResponseUtils;
import com.gavinjin.backend.common.StatusCode;
import com.gavinjin.backend.exception.BusinessException;
import com.gavinjin.backend.model.domain.Team;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.model.dto.TeamQuery;
import com.gavinjin.backend.model.request.TeamAddRequest;
import com.gavinjin.backend.model.request.TeamJoinRequest;
import com.gavinjin.backend.model.request.TeamQuitRequest;
import com.gavinjin.backend.model.request.TeamUpdateRequest;
import com.gavinjin.backend.model.vo.TeamUserVO;
import com.gavinjin.backend.service.TeamService;
import com.gavinjin.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Team controller
 */
@RestController
@RequestMapping("/team")
@Slf4j
@CrossOrigin()
public class TeamController {
    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Long> createTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        User loginUser = userService.getLoginUser(request);
        long teamId = teamService.createTeam(team, loginUser);
        return ResponseUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(long id) {
        if (id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        boolean deleted = teamService.removeById(id);
        if (!deleted) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Fail to delete a new team");
        }
        return ResponseUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        boolean updated = teamService.updateTeam(teamUpdateRequest, loginUser );
        if (!updated) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Fail to update a new team");
        }
        return ResponseUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if (id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(StatusCode.NULL_ERROR);
        }
        return ResponseUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, isAdmin);
        return ResponseUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);

        long pageSize = teamQuery.getPageSize();
        long current = teamQuery.getPageNum();
        Page<Team> page = new Page<>(current, pageSize);

        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        return ResponseUtils.success(resultPage);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResponseUtils.success(result);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResponseUtils.success(result);
    }
}
