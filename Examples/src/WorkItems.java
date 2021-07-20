import filenet.vw.api.*;
import org. apache. log4j. *;
public class WorkItems {
	 
	private static VWSession peSession=null;
	private static final Logger log = LogManager.getLogger(WorkItems.class.getName());
	
	 public static VWSession getPESession()
	 {
	      String strAppURI1="http://localhost:9080/wsi/FNCEWS40MTOM/";
	      System.out.println("[ENTER  PEManager getPESession()]");
	      System.setProperty("java.security.auth.login.config","C:\\opt\\jaas.conf.WSI");

	      try
	      {
	        peSession = new VWSession();
	        peSession.setBootstrapCEURI(strAppURI1);
	        peSession.logon("p8admin", "Password1", "WFCP");
	        String sn = peSession.getPEServerName();
	        System.out.println("++++++++++++++++"+ sn);
	        System.out.println("PE session established:"+peSession);
	      }
	      catch (VWException e) {
	          System.out.println("Exception occured while establishing PE session." );
	          e.printStackTrace();
	      }
	      System.out.println("[Exit PEManager getPESession()]");
	    return peSession;
	  }
	 
	//Close VWSession
	public static void closePESession(VWSession peSession)
	{
	    log.debug("[Enter closePESession]");
	    try {
	      if (peSession != null)
	        peSession.logoff();
	    }
	    catch (VWException e) {
	      log.error(e.getMessage(), e);
	    }
	    log.debug("[Exit : closePESession]");
	} 
	public static void main(String[] args ){
		getPESession();
	    VWQueue queue;
	    int queryFlag;
	    int queryType;
	    VWQueueQuery query;
	    int counter;
	    VWQueueElement queueElement;
	    
		queue = peSession.getQueue("Inbox");
        queryFlag = VWQueue.QUERY_NO_OPTIONS;
        queryType = VWFetchType.FETCH_TYPE_QUEUE_ELEMENT;
        query = queue.createQuery(null,null,null,queryFlag, null,null,queryType);
        counter = 0;
        System.out.println( "Inbox: " + queue.fetchCount() + 
        		"    Query: " + query.fetchCount());
        
        while (query.hasNext()) {
        	
            counter++;
            
            queueElement = (VWQueueElement) query.next();
            System.out.println(counter+": (" + queueElement.getWorkObjectNumber()+") "
            		
            + queueElement.getFieldValue("F_BoundUser")+", " + queueElement.getWorkflowName()
            
            +", " + queueElement.getFieldValue("F_Subject")+", " 
            
            + queueElement.getFieldValue("F_LockTime") );
            
        }
        System.out.println();
	}
}
