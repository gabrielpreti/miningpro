package com.miningpro.run;

import org.apache.camel.builder.RouteBuilder;

public class PrintSlots extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file:/tmp/slots/report/events/").beanRef("httpEventsRepository", "printSlotEvents");
        from("file:/tmp/slots/report/metrics/").beanRef("httpEventsRepository", "printSlotMetrics");

	}

}
