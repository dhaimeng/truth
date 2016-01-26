package Analysis;

import java.util.ArrayList;

public class text {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList sb=new ArrayList();
		String pre="<http://oneobjectString> <http://oneobject> Beijing (China)@en .";
      sb.add(pre);
      sb.add(pre);
      sb.add("ds");
      sb.add(pre);
      sb.add(pre);
      sb.add(pre);
      sb.add(pre);
      sb.add("ds");
     // sb.remove(1);
      for(int i1=0;i1<sb.size();i1++){
          System.out.println("hh"+sb.get(i1).toString());
          }
      for(int i1=0;i1<sb.size();i1++){
		  if(i1<sb.size()-1){
		      if(sb.get(i1).toString().indexOf("http://oneobject")!=-1&&sb.get(i1+1).toString().indexOf("http://oneobject")!=-1){ 
		    	  sb.remove(i1);
		    	  i1--;
		         }				 
		      }else{
			 if(sb.get(i1).toString().indexOf("<http://oneobject>")!=-1)
				 sb.remove(i1);
		 }
	  }
      for(int i1=0;i1<sb.size();i1++){
      System.out.println(sb.get(i1).toString());
      }
	}

}
