package com.gvolpe.streams.flows.utils

import akka.stream.UniformFanOutShape
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL}
import com.gvolpe.streams.flows.{FlowMessage, MessageHeader}

object PartialFlowGraphUtils {

  def partialFlow(function: FlowMessage => FlowMessage) = Flow[FlowMessage] map (function(_))

  def partialFlowWithHeader(header: MessageHeader) = partialFlow(e => addHeader(e, header))

  def filterPartialFlowGraph(filterFunction: FlowMessage => Boolean) = GraphDSL.create() { implicit b =>
    val bcast = b.add(Broadcast[FlowMessage](2))
    val filter = b.add(Flow[FlowMessage] filter (filterFunction(_)))
    val notFilter = b.add(Flow[FlowMessage] filter (!filterFunction(_)))

    bcast ~> filter
    bcast ~> notFilter

    UniformFanOutShape(bcast.in, filter.outlet, notFilter.outlet)
  }

  def addHeader(message: FlowMessage, header: MessageHeader): FlowMessage = {
    val headers = message.headers + (header.key -> header.value)
    message.copy(headers, message.event)
  }

}
