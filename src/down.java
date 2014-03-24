import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class down {
    public static void main(String[] args) throws Exception 
{
    	 
String page,ne,l,url="http://www.webmd.com/a-to-z-guides/common-topics/";
ne=url+"default.htm";
int tem=0;
char a='a';
down d=new down();

for(int i=1;i<26;i++)
{
	//System.out.println(ne);
	page=d.dun(ne);
	d.page_process(page,a);
	tem=97+i;
	a=(char)tem;
	ne=url+a+".htm";
}
//	System.out.println(ne);
	page=d.dun(ne);
	d.page_process(page,a);
	
	
	//open file for each a-z
//for now let us cal a function dat does this process
	for(int i=0;i<26;i++)
	{
		tem=97+i;
		a=(char)tem;
		try{
			BufferedReader br=new BufferedReader(new FileReader(a+".txt"));
			String ap;
			int counter=1;
			while((ap = br.readLine())!=null)
			{
				int k=ap.indexOf("link:");
				String dis=ap.substring(0,k-1).trim();
				String link=ap.substring(k+5);
				d.eachdisease(dis,link,'1',a,counter);
				counter++;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	




d.eachdisease("aids","http://www.webmd.com/hiv-aids/default.htm",'1','a',1);

}

    public void eachdisease(String dis,String link,char a,char ch,int counter)throws Exception 
    {
//    	System.out.println(dis+"----"+link);
    	try
    	{
    	BufferedWriter out = new BufferedWriter(new FileWriter(ch+"_"+counter+"_"+a+".html"));
    	down d=new down();
    	String sd;
    	if(a=='1')
    	sd=d.dun(link);
    	else
    	sd=d.dun(link+"?page="+a);
    	
    	String cm=d.impinfo(sd);
    	out.write(dis+"\n");
    	out.write(cm);
    	out.close();
    	
    	if(sd.contains("?page="+(char)(a+1)))
    	{
    		d.eachdisease(dis, link,(char)(a+1),ch,counter);
    		
    	}	
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    	
    	
    }
    
    
    public String impinfo(String page)throws Exception 
    {
    	
    	Document doc = Jsoup.parseBodyFragment(page);
    	Elements elem= doc.getElementsByAttributeValue("id", "mainContent_area");
 
    	String n=elem.text();
    	System.out.println(n);
    	return n;
    }
    
    
    
    public void page_process(String page,char a)throws Exception 
    {
    	
      BufferedWriter out = new BufferedWriter(new FileWriter(a+".txt"));
  	       
    	//Document doc = Jsoup.parse(page);
    	
    	Document doc = Jsoup.parseBodyFragment(page);
    	Element body = doc.body();
    	Elements elem= doc.getElementsByAttributeValue("class", "a-to-z list");
    	String n=elem.html();
    	Document doc1 = Jsoup.parseBodyFragment(n);
    	Elements elem1=doc1.getElementsByTag("ul");
    //	System.out.println(elem1.html());
    	String m=elem1.html();
    	Document doc2 = Jsoup.parseBodyFragment(m);
    	Elements elem2=doc2.select("a");
    //	System.out.println(elem2.html());
    	for(Element el: elem2)
    	{
    //	System.out.println(el.html()+"   link:http://www.webmd.com"+el.attr("href"));
    	
    //		System.out.println("hoooooooooo"+el.attr("href"));
    		if(el.attr("href").toString().startsWith("http://"))
    		out.write(el.html()+"   link:"+el.attr("href")+"\n");
    		else
    		out.write(el.html()+"   link:http://www.webmd.com"+el.attr("href")+"\n");
    	}
    	 out.close();
    //	System.out.println("------------------------------------------------");
    }
    
    public String dun(String url)throws Exception 
    {
   		 System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
   		 System.setProperty("http.proxyPort", "8080");
   		 
   		 Document doc = Jsoup.connect(url).timeout(0).get();
   	        String html = doc.html();
   	        return html;
   	    //    BufferedWriter out = new BufferedWriter(new FileWriter("/home/arnav/Desktop/corpus/corp.html"));
   	     //   out.write(html);
   	      //  out.close();
    }
}