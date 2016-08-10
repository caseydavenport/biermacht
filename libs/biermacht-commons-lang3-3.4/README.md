This builds the biermacht.org.apache.commons jar from the org.apache.commons jar.


To generate the jar:
```
java -jar jarjar-1.4.jar process commons-codec.rules commons-lang3-3.4.jar biermacht-commons-lang3-3.4.jar
```


References:

http://stackoverflow.com/questions/33502203/android-studio-java-lang-nosuchmethoderror-with-an-imported-library
http://blog.osom.info/2015/04/commons-codec-on-android.html

jarjar: https://code.google.com/archive/p/jarjar/downloads
