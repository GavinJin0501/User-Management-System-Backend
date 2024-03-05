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
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        boolean updated = teamService.updateById(team);
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
    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);

        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> teamList = teamService.list(queryWrapper);
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
}
