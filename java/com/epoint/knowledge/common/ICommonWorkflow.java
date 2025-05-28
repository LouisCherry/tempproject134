/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.knowledge.common;

import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowTransitionInstance;

public interface ICommonWorkflow {
	WorkflowTransitionInstance getTansitionBySrcActIns(String arg0);

	WorkflowActivity getActivityByGuid(String arg0);

	String generateSerialNum(String arg0);

	String getActivityGuidByTransitionGuid(String arg0);
}