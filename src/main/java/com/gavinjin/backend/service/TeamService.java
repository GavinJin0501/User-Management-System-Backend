package com.gavinjin.backend.service;

import com.gavinjin.backend.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gavinjin.backend.model.domain.User;

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
}
