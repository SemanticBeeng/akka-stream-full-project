package com.gvolpe.streams.flows

import akka.stream.FlowShape
import akka.stream.scaladsl.GraphDSL
import akka.stream.scaladsl.GraphDSL.Implicits._
import com.gvolpe.streams.flows.TransformerFlows._
import com.gvolpe.streams.flows.utils.PartialFlowGraphUtils._

trait EventTypeFilteredFlow {

  lazy val eventTypeFilteredFlow = GraphDSL.create() { implicit b =>
    val logger = b.add(partialFlowWithHeader(MessageHeader("suppressed", "EventTypeFilter")))
    val sender = b.add(partialFlow(sendToExternalService(_)))

    logger ~> sender

    FlowShape(logger.in, sender.outlet)
  }.named("eventTypeFilteredFlow")

}
