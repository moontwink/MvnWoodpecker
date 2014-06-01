# MvnWoodpecker
## 
### A Maven Build of Woodpecker

**Changes from TheWoodpecker:**

 * Maven Build
 * Integrated Twitter Crawler (update: already connected with database)
 * Dependency Management


To run this, we'll have to do a number of stuff.

Lookie heree ~~~

 1. Open project in Netbeans
 2. Yes, there will be exclamations and warning signs.
 3. To fix that, expand project > dependencies (right click) > resolve 
	** or CLEAN > BUILD **
 4. If it fails, it's probably because of internet issues. (probably)
 5. Since the mallet jar file from the apache repository is ~weirdly~ different from what we're using, click on the **mallet-2.0.7** dependency and click manually install artifact. Browse into the **lib** directory of the project and click the **mallet.jar**. That should fix the issue with mallet. 
 6. Next is the twitter4j-stream dependency. Now you're gonna need this [Twitter4JCrawler](https://github.com/moontwink/Twitter4JCrawler.git) for the twitter4j files necessary, so download/clone that. 

 6.1 Now, open the them in the following order **twitter4j-core** > **twitter4j-async** > **twitter4j-stream**

 6.1.1 Clean > Build > Clean and Build in the same order until warning signs are gone. 
 
 6.2 Go back to **MvnWoodpecker** and right click on **Dependencies** > **Add Dependency** > got to the **Open Projects** tab and add the **twitter4j-stream with version 4.0.2-SNAPSHOT** 
 
 7. Hmmmm.... things should be fine at this point already. 

And just change the DBFactory password and it should work!

```javascript
	static String username = "root";
    static String password = ""; //"p@ssword";
```

This is [on GitHub](https://github.com/moontwink/MvnWoodpecker) so let me know if something doesn't work with yours.


