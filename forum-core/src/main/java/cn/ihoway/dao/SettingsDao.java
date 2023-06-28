package cn.ihoway.dao;

import cn.ihoway.entity.Settings;

public interface SettingsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Settings record);

    int insertSelective(Settings record);

    Settings selectByPrimaryKey(Integer id);

    int updateByUserSelective(Settings record);

    int updateByPrimaryKey(Settings record);
}