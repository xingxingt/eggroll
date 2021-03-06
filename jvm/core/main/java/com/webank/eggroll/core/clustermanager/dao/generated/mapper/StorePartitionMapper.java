/*
 * Copyright (c) 2019 - now, Eggroll Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.webank.eggroll.core.clustermanager.dao.generated.mapper;

import com.webank.eggroll.core.clustermanager.dao.generated.model.StorePartition;
import com.webank.eggroll.core.clustermanager.dao.generated.model.StorePartitionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface StorePartitionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    long countByExample(StorePartitionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int deleteByExample(StorePartitionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long storePartitionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int insert(StorePartition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int insertSelective(StorePartition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    List<StorePartition> selectByExampleWithRowbounds(StorePartitionExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    List<StorePartition> selectByExample(StorePartitionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    StorePartition selectByPrimaryKey(Long storePartitionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") StorePartition record, @Param("example") StorePartitionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") StorePartition record, @Param("example") StorePartitionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(StorePartition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table store_partition
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(StorePartition record);
}