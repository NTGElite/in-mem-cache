# maven-in-mem-cache-library
A non-executable maven project just for building jar file that will be imported into other applications for taking advantages of in memory cache.

The project is built and deployed to Github packages on merging onto master.

## Installation
Please take a look at this guide carefully to be able to install maven dependencies published to GitHub Packages
https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry

After configuring properly your maven settings as instructed in the link above. Add this to your `pom.xml` and simply run `maven install` afterward and that should be it.
```
<dependency>
  <groupId>com.ntgelite</groupId>
  <artifactId>in-mem-cache-library</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

You may get a glance of this https://github.com/NTGElite/in-mem-cache/packages/1993131 for more information.