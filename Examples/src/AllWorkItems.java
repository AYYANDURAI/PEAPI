import filenet.vw.api.*;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org. apache. log4j. *;
public class AllWorkItems {
	 
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
	    VWRoster roster;
	    int queryFlag;
	    int queryType;
	    VWRosterQuery query;
	    //int counter;
	    VWWorkObject workObject;
	    
		roster = peSession.getRoster("DefaultRoster");
        queryFlag = VWRoster.QUERY_NO_OPTIONS;
        queryType = VWFetchType.FETCH_TYPE_WORKOBJECT;
        query = roster.createQuery(null,null,null,queryFlag, null,null,queryType);
        //counter = 0;
        System.out.println( "Inbox: " + roster.fetchCount() + 
        		"    Query: " + query.fetchCount());
        
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
   
        while (query.hasNext()) {
            //counter++;
            workObject = (VWWorkObject) query.next();
            
            map.put(workObject.getWorkObjectNumber(), new ArrayList<String>());
            
            if(workObject.getLockedUser()!=null){
            	//System.out.println(workObject.getLockedUser());
            	map.get(workObject.getWorkObjectNumber()).add("Locked"); 
            	map.get(workObject.getWorkObjectNumber()).add(workObject.getLockedUser()); 
            	//System.out.println(workObject.getFieldNames());
            }else{
            	map.get(workObject.getWorkObjectNumber()).add("Not Locked"); 
            	map.get(workObject.getWorkObjectNumber()).add("None"); 
            }
          
            
        }
       
        System.out.print(map);
        	
        System.out.println();
	}
}
