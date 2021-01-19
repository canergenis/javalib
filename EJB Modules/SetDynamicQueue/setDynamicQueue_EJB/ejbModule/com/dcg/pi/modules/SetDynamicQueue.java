package com.hayat.module;

import java.io.InputStream;

import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.sap.aii.af.lib.mp.module.Module;
import com.sap.aii.af.lib.mp.module.ModuleContext;
import com.sap.aii.af.lib.mp.module.ModuleData;
import com.sap.aii.af.lib.mp.module.ModuleException;
import com.sap.engine.interfaces.messaging.api.Message;
import com.sap.engine.interfaces.messaging.api.MessageKey;
import com.sap.engine.interfaces.messaging.api.PublicAPIAccessFactory;
import com.sap.engine.interfaces.messaging.api.XMLPayload;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditAccess;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditLogStatus;

/**
 * Session Bean implementation class SetDynamicQueue
 */
@Stateless
public class SetDynamicQueue implements Module {

    /**
     * Default constructor. 
     */
    public SetDynamicQueue() {
        // TODO Auto-generated constructor stub
    }
    
    public ModuleData process(ModuleContext mc, ModuleData md) throws ModuleException {
    	AuditAccess audit = null;
    	Message msg = null;
    	MessageKey mk = null;
    	
    	try {
    		msg = (Message) md.getPrincipalData();
    		mk = msg.getMessageKey();
    		
    		audit = PublicAPIAccessFactory.getPublicAPIAccess().getAuditAccess();
    		audit.addAuditLogEntry(mk, AuditLogStatus.SUCCESS, "SetDynamicQueue module called");

    	} catch(Exception e) {
    		ModuleException me = new ModuleException(e);
    		throw me;
    	}
    	
		XMLPayload xp = msg.getDocument();
    	
    	if (xp != null) {
    		InputStream is = xp.getInputStream();
    		
    		try {
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
				XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile(mc.getContextData("XPath"));
				
	    		audit.addAuditLogEntry(mk, AuditLogStatus.SUCCESS, "Module parameter value received: " + mc.getContextData("XPath"));
				
				String queue = "Q_" + xPathExpression.evaluate(doc, XPathConstants.STRING).toString();
				msg.setSequenceId(queue);	
				
	    		audit.addAuditLogEntry(mk, AuditLogStatus.SUCCESS, "Dynamic queue name added successfully: " + queue);
					
			} catch (Exception e) {
	    		audit.addAuditLogEntry(mk, AuditLogStatus.ERROR, "Cannot assign the dynamic queue");
	    		ModuleException me = new ModuleException(e);
	    		throw me;
	    		
			}
    	}
    	
    	return md;
    }
}