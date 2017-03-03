package com.gvolpe.streams.flows

import akka.stream.UniformFanOutShape
import akka.stream.scaladsl.GraphDSL
import akka.stream.scaladsl.GraphDSL.Implicits._
import com.gvolpe.streams.flows.utils.PartialFlowGraphUtils._

trait EventInputFlow {

  this: EventTypeFilteredFlow =>

  lazy val eventInputFlow = GraphDSL.create() { implicit b =>
    val headersProcess = b.add(partialFlowWithHeader(MessageHeader("starting", System.currentTimeMillis())))

    val eventTypeFilterFlow = b.add(filterPartialFlowGraph(_.event.`type` == "TENNIS"))
    val headersFilterFlow = b.add(filterPartialFlowGraph(_.headers.contains("MatchSession")))
    val eventTypeFiltered = b.add(eventTypeFilteredFlow)

    headersProcess ~> eventTypeFilterFlow
                      eventTypeFilterFlow.out(0) ~> headersFilterFlow
                      eventTypeFilterFlow.out(1) ~> eventTypeFiltered

    UniformFanOutShape(headersProcess.in, headersFilterFlow.out(0), headersFilterFlow.out(1), eventTypeFiltered.outlet)
  }.named("eventInputFlow")

}
