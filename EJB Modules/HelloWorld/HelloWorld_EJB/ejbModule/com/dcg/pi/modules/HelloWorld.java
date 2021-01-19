package com.dcg.pi.modules;

import javax.ejb.Stateless;

import com.sap.aii.af.lib.mp.module.Module;
import com.sap.aii.af.lib.mp.module.ModuleContext;
import com.sap.aii.af.lib.mp.module.ModuleData;
import com.sap.aii.af.lib.mp.module.ModuleException;
import com.sap.engine.interfaces.messaging.api.Message;
import com.sap.engine.interfaces.messaging.api.MessageKey;
import com.sap.engine.interfaces.messaging.api.PublicAPIAccessFactory;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditAccess;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditLogStatus;
import com.sap.engine.interfaces.messaging.api.exception.MessagingException;

/**
 * Session Bean implementation class HelloWorld
 */
@Stateless
public class HelloWorld implements Module {

    /**
     * Default constructor. 
     */
    public HelloWorld() {
    	
    }

	@Override
	public ModuleData process(ModuleContext mc, ModuleData md) throws ModuleException {
    	AuditAccess audit = null;
    	
    	Message msg = (Message) md.getPrincipalData();
    	MessageKey mk = msg.getMessageKey();
		
		try {
			audit = PublicAPIAccessFactory.getPublicAPIAccess().getAuditAccess();
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		audit.addAuditLogEntry(mk, AuditLogStatus.SUCCESS, "HelloWorld");
		
		return md;
	}
}