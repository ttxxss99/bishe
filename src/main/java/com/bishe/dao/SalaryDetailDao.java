package com.bishe.dao;

import com.bishe.model.SalaryDetail;
import com.bishe.model.vo.SalaryDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalaryDetailDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table salary_detail
     *
     * @mbg.generated Fri Jan 10 10:54:28 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table salary_detail
     *
     * @mbg.generated Fri Jan 10 10:54:28 CST 2020
     */
    int insert(SalaryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table salary_detail
     *
     * @mbg.generated Fri Jan 10 10:54:28 CST 2020
     */
    List<SalaryDetailVo> selectByPrimaryKey(SalaryDetailVo salaryDetailVo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table salary_detail
     *
     * @mbg.generated Fri Jan 10 10:54:28 CST 2020
     */
    List<SalaryDetailVo> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table salary_detail
     *
     * @mbg.generated Fri Jan 10 10:54:28 CST 2020
     */
    int updateByPrimaryKey(SalaryDetail record);
}