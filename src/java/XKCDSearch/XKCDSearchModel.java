/**
 *
 * @author ankur
 * This file is the Model componet of the MVC, and it models the business logic
 * for the  web application. In this case, the business logic involves making a request to 
 * xkcdd.com/archive and then screen scraping the HTML to return the URL of s matched comic
 * randomly
 */

package XKCDSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class XKCDSearchModel {
    
    //total count of the comics on the website
    private int totalCount = 0;
    
    //the url for the  comic image
    private String resultUrl = null;
    
    // the coount of the total matched comics
    private int searchCount = 0;
    
    //comicName which is randomly chosen from the matched comics
    private String comicName = null;
    
    /**
     * 
     * @param search the keyword to be searched for in the  comic names
     * @throws UnsupportedEncodingException 
     */
    public void doXKCDSearch(String search)
        throws UnsupportedEncodingException {
        
        // we will store the html content from the webpage here
        String response = "";
        
        //Create a URL for the page to be screen scraped
        String Url = "https://xkcd.com/archive/";
        
        //Fetch the page
        response = fetch(Url);
        
        //arraylist to store all the matched comics, to randomly chcose one from later
        List<String> comicNumbers = new ArrayList<String>();
        
        /*
         * Search the page to find the comic URL
         *
         *
         * These particular searches were crafted by carefully looking at 
         * the HTML that XKCD returned, and finding (by experimentation) the
         * generalizable steps that will reliably get a comic URL.
         * 
         * First do a String search that gets me close to the comic URL target
         */
        
        //used to maintain the start index of each comic entry
        int newComicStartIndex = 0;
        //used to maintain the end index of each comic entry
        int newComicEndIndex = 0;
        
        // from which index to start the search for comic name
        // the middleContainer box is from wehre the comic names start, so let's start are screen scraoing beyond this point 
        int fromIndex = response.indexOf("middleContainer");
        
        //to maintain the total count of the comics
        int count = 0;
        
        //to get the comic name for each found entry in the html page
        String s = new String();
        
        // the last index till where we will scrape the webpage
        int windowStop = response.indexOf("<div id=\"bottom");
        
        //the index of the first <a href found
        //this will be close to our first comic name
        newComicStartIndex = response.indexOf("<a href=", fromIndex);
        
        //continue to scrape until we reach the end of the block
        while((newComicStartIndex < windowStop))
        {
            //after "<a href" search for the title=" keyword  as just beofre it we will have the comic number
            newComicEndIndex = response.indexOf("title=\"", newComicStartIndex);
            //search for the closing / after <ahref that is where our comic number ends
            newComicEndIndex = response.indexOf("/", newComicStartIndex + 10);
            // get the comic number which we wiil use for the url if the comic matched
            String bookNumber = response.substring(newComicStartIndex + 10, newComicEndIndex);
            //search for > to find the index from wehre the comic name starts
            newComicStartIndex = response.indexOf(">",newComicEndIndex)+1;
            //search for </a> tag which will depict the end of this comic name
            newComicEndIndex = response.indexOf("</a>",newComicStartIndex);
           //get the comic name
            s = response.substring(newComicStartIndex,newComicEndIndex);
            //if comic name has search string pattern in it, then store it
            if(s.toLowerCase().contains(search.toLowerCase())){
                System.out.println(s);
                comicNumbers.add(bookNumber);
            }
            
            //increase the from index to start screen scraping for the next comic
            fromIndex = newComicEndIndex;
            //maintain the total count of comics 
            count++;
            // when the book number 1 is found, that means we are done finding all of the comics 
            if(bookNumber.equals("1")) break;
            //keep iterating to next <a href until book number 1s not found
            newComicStartIndex = response.indexOf("<a href=\"/", fromIndex);
            // If not found, then no such photo is available.
        }
        
        //strore all the values in the object of the class
        this.totalCount = count;
        //find the number of all the matched comics found
        this.searchCount = comicNumbers.size();
        
        //if no comics are found no need to get url
        if(this.searchCount == 0) return;
        //generate a random number within our array length to slect a random comic to display
        int randomInt = 0 + (int)(Math.random() * ((this.searchCount-1)+1));
        //build the url for the randomly selected matched  comic
        String comicUrl = "https://xkcd.com/" + comicNumbers.get(randomInt) + "/"; 
        
        // Now, crawl to this Url and get the URL for the picture of comic
        this.resultUrl = doComicPictureSearch(comicUrl);
        
        // Now, crawl to this Url again and get the name of the comic
        this.comicName = doComicNameSearch(comicUrl);
    }
    
    /**
     * 
     * @param comicUrl the comic url to web scrape to get the comic name and the comic image link
     * @return the comic image url
     */
    private String doComicPictureSearch(String comicUrl){
        String response = "";
        //fetch the html page
        response = fetch(comicUrl);
        //System.out.println(response);
        
        //search for the image url of the comic
        int cutLeft = response.indexOf("Image URL (for hotlinking/embedding): ");
        if(cutLeft == -1) return (String)null;
        cutLeft += "Image URL (for hotlinking/embedding): ".length();
        int cutRight = response.indexOf("<div id", cutLeft);
        return response.substring(cutLeft, cutRight);
    }
    
    /**
     * this methid takes in the comic url to return the comic name after web scraping
     * @param comicUrl url for the comic
     * @return the comic name
     */
    private String doComicNameSearch(String comicUrl){
        String response = "";
        //fetch the html comic page
        response = fetch(comicUrl);
        //System.out.println(response);
        //fiind the comic name by web scraping
        int cutLeft = response.indexOf("<div id=\"ctitle\">");
        if(cutLeft == -1) return (String)null;
        cutLeft += "<div id=\"ctitle\">".length();
        int cutRight = response.indexOf("</div>", cutLeft);
        return response.substring(cutLeft, cutRight);
    }
    
    /**
     * Fetched the html content of the wepage
     * @param Url the url to get the html content of
     * @return the string of html content
     */
    private String fetch(String Url){
        String response = "";
        try{
            URL finalUrl = new URL(Url);
        /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which 
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            HttpURLConnection connection = (HttpURLConnection) finalUrl.openConnection();
            if(connection.getResponseCode() != 200) return null;
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response += str;
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Found an exception");
        }
        return response;
    }
    
    /**
     * 
     * @return totalcount of matched comics 
     */
    public int getTotalCount(){
        return this.totalCount;
    }
    
    /**
     * 
     * @return the resulturl of the comic page
     */
    public String getResultUrl(){
        return this.resultUrl;
    }
    
    /**
     * 
     * @return the search count of matched comics 
     */
    public int getSearchCount(){
        return this.searchCount;
    }
    
    /**
     * 
     * @return the ocmic name randomly selected 
     */
    public String getComicName(){
        return this.comicName;
    }
}
