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

package com.webank.eggroll.rollpair.component

import com.webank.eggroll.core.command.{CollectiveCommand, CommandURI}
import com.webank.eggroll.core.constant.StringConstants
import com.webank.eggroll.core.meta._
import com.webank.eggroll.core.meta.MetaModelPbSerdes._
import com.webank.eggroll.core.schedule.{JobRunner, JoinTaskPlan, ListScheduler, MapTaskPlan, ReduceTaskPlan, ShuffleTaskPlan}
import com.webank.eggroll.core.serdes.DefaultScalaSerdes

import scala.collection.mutable

class RollPairService() {
  val scheduler = ListScheduler()

  def mapValues(inputJob: ErJob): ErStore = {
    // f: Array[Byte] => Array[Byte]
    // val functor = ErFunctor("map_user_defined", functorSerDes.serialize(f))

    val inputStore = inputJob.inputs.head
    val inputLocator = inputStore.storeLocator
    val outputLocator = inputLocator.copy(name = "testMapValues")

    val inputPartitionTemplate = ErPartition(id = "0", storeLocator = inputLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))
    val outputPartitionTemplate = ErPartition(id = "0", storeLocator = outputLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))

    val numberOfPartitions = 4

    val inputPartitions = mutable.ListBuffer[ErPartition]()
    val outputPartitions = mutable.ListBuffer[ErPartition]()

    for (i <- 0 until numberOfPartitions) {
      inputPartitions += inputPartitionTemplate.copy(id = i.toString)
      outputPartitions += outputPartitionTemplate.copy(id = i.toString)
    }

    // todo: move to cluster manager
    val inputStoreWithPartitions = inputStore.copy(storeLocator = inputLocator,
      partitions = inputPartitions.toList)
    val outputStoreWithPartitions = inputStore.copy(storeLocator = outputLocator,
      partitions = outputPartitions.toList)

    val job = inputJob.copy(inputs = List(inputStoreWithPartitions), outputs = List(outputStoreWithPartitions))

    val taskPlan = new MapTaskPlan(new CommandURI(RollPairService.eggMapValuesCommand), job)

    scheduler.addPlan(taskPlan)

    // todo: update database
    // val xxx = updateOutputDb()

    JobRunner.run(scheduler.getPlan())

    outputStoreWithPartitions
  }

  // todo: give default partition function: hash and mod
  def map(inputJob: ErJob): ErStore = {
    val inputStore = inputJob.inputs.head
    val inputLocator = inputStore.storeLocator
    val outputLocator = inputLocator.copy(name = "testMap")

    val inputPartitionTemplate = ErPartition(id = "0", storeLocator = inputLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))
    val outputPartitionTemplate = ErPartition(id = "0", storeLocator = outputLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))

    val numberOfPartitions = 4

    val inputPartitions = mutable.ListBuffer[ErPartition]()
    val outputPartitions = mutable.ListBuffer[ErPartition]()

    for (i <- 0 until numberOfPartitions) {
      inputPartitions += inputPartitionTemplate.copy(id = i.toString)
      outputPartitions += outputPartitionTemplate.copy(id = i.toString)
    }

    val inputStoreWithPartitions = inputStore.copy(storeLocator = inputLocator,
      partitions = inputPartitions.toList)
    val outputStoreWithPartitions = inputStore.copy(storeLocator = outputLocator,
      partitions = outputPartitions.toList)

    val job = inputJob.copy(inputs = List(inputStoreWithPartitions), outputs = List(outputStoreWithPartitions))

    val taskPlan = new ShuffleTaskPlan(new CommandURI(RollPairService.eggMapCommand), job)
    scheduler.addPlan(taskPlan)

    JobRunner.run(scheduler.getPlan())

    outputStoreWithPartitions
  }

  def reduce(inputJob: ErJob): ErStore = {
    val inputStore = inputJob.inputs.head
    val inputLocator = inputStore.storeLocator
    val outputLocator = inputLocator.copy(name = "testReduce")

    val inputPartition = ErPartition(id = "0", storeLocator = inputLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))
    val outputPartition = ErPartition(id = "0", storeLocator = outputLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))

    val inputStoreWithPartitions = inputStore.copy(storeLocator = inputLocator,
      partitions = List(inputPartition.copy(id = "0"), inputPartition.copy(id = "1")))

    val outputStoreWithPartitions = inputStore.copy(storeLocator = outputLocator,
      partitions = List(outputPartition))

    val job = inputJob.copy(inputs = List(inputStoreWithPartitions), outputs = List(outputStoreWithPartitions))

    val taskPlan = new ReduceTaskPlan(new CommandURI(RollPairService.eggReduceCommand), job)
    scheduler.addPlan(taskPlan)

    JobRunner.run(scheduler.getPlan())

    outputStoreWithPartitions
  }

  def join(inputJob: ErJob): ErStore = {
    val leftStore = inputJob.inputs.head
    val leftLocator = leftStore.storeLocator

    val rightStore = inputJob.inputs(1)
    val rightLocator = rightStore.storeLocator

    val outputLocator = leftLocator.copy(name = "testJoin")

    val leftPartitionTemplate = ErPartition(id = "0", storeLocator = leftLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))
    val rightPartitionTemplate = ErPartition(id = "0", storeLocator = rightLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))
    val outputPartitionTemplate = ErPartition(id = "0", storeLocator = outputLocator, node = ErServerNode(commandEndpoint = ErEndpoint("localhost", 20001)))

    val numberOfPartitions = 4

    val leftPartitions = mutable.ListBuffer[ErPartition]()
    val rightPartitions = mutable.ListBuffer[ErPartition]()
    val outputPartitions = mutable.ListBuffer[ErPartition]()

    for (i <- 0 until numberOfPartitions) {
      leftPartitions += leftPartitionTemplate.copy(id = i.toString)
      rightPartitions += rightPartitionTemplate.copy(id = i.toString)
      outputPartitions += outputPartitionTemplate.copy(id = i.toString)
    }

    val leftStoreWithPartitions = leftStore.copy(partitions = leftPartitions.toList)
    val rightStoreWithPartitions = rightStore.copy(partitions = rightPartitions.toList)
    val outputStoreWithPartitions = ErStore(storeLocator = outputLocator, partitions = outputPartitions.toList)

    val job = inputJob.copy(inputs = List(leftStoreWithPartitions, rightStoreWithPartitions), outputs = List(outputStoreWithPartitions))
    val taskPlan = new JoinTaskPlan(new CommandURI(RollPairService.eggJoinCommand), job)
    scheduler.addPlan(taskPlan)

    JobRunner.run(scheduler.getPlan())

    outputStoreWithPartitions
  }
}

object RollPairService {
  val clazz = classOf[RollPairService]
  val functorSerDes = DefaultScalaSerdes()

  val map = "map"
  val mapValues = "mapValues"
  val reduce = "reduce"
  val join = "join"
  val rollPair = "RollPair"
  val eggPair = "EggPair"

  val runTask = "runTask"
  val eggMapCommand = s"${eggPair}.${map}"
  val eggMapValuesCommand = s"${eggPair}.${mapValues}"
  val eggReduceCommand = s"${eggPair}.${reduce}"
  val eggJoinCommand = s"${eggPair}.${join}"

  val rollMapCommand = s"${rollPair}.${map}"
  val rollMapValuesCommand = s"${rollPair}.${mapValues}"
  val rollReduceCommand = s"${rollPair}.${reduce}"
  val rollJoinCommand = s"${rollPair}.${join}"

  /*  CommandRouter.register(mapCommand,
      List(classOf[Array[Byte] => Array[Byte]]), clazz, "mapValues", null, null)*/
}