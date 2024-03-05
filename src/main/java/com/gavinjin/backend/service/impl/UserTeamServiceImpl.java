package com.gavinjin.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.backend.model.domain.UserTeam;
import com.gavinjin.backend.service.UserTeamService;
import com.gavinjin.backend.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author gavin
* @description 针对表【user_team(User-Team relation table)】的数据库操作Service实现
* @createDate 2024-03-04 23:19:25
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




