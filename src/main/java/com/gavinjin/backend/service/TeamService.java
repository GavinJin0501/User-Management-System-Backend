package com.gavinjin.backend.service;

import com.gavinjin.backend.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.model.dto.TeamQuery;
import com.gavinjin.backend.model.request.TeamJoinRequest;
import com.gavinjin.backend.model.request.TeamQuitRequest;
import com.gavinjin.backend.model.request.TeamUpdateRequest;
import com.gavinjin.backend.model.vo.TeamUserVO;

import java.util.List;

/**
* @author gavin
* @description 针对表【team(Team table)】的数据库操作Service
* @createDate 2024-03-04 23:18:13
*/
public interface TeamService extends IService<Team> {
    /**
     * Create a team
     *
     * @param team
     * @param loginUser
     * @return
     */
    long createTeam(Team team, User loginUser);

    /**
     * Search teams
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * Update a team
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * Join a team
     *
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * Quit a team
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);
}
