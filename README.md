# jhotdraw

[![Java CI with Maven](https://github.com/wumpz/jhotdraw/actions/workflows/maven.yml/badge.svg)](https://github.com/wumpz/jhotdraw/actions/workflows/maven.yml)

JHotDraw is a Java-based graphics library that allows developers to create and manipulate graphical objects such as shapes, figures, and diagrams. The library has been in development since the mid-1990s and has been used in a variety of applications including drawing programs, diagram editors, and UML tools.


## News

> **ATTENTION**: Due to the refactoring in 10.0-SNAPSHOT this version breaks API of JHotdraw. Some adaptions are needed, e.g.: attributes now using `attr()`, ...

## Changes
The recent update to JHotDraw includes several significant changes aimed at improving the library's performance, reliability, and maintainability. Here are some more details on the changes:

* heavy restructuring of classes and interfaces and cleanup
  * complete attribute handling of Figure moved in class Attributes, access over **attr()**
  * Drawing has no dependency to CompositeFigure anymore and implementations do not use 
   AbstractCompositeFigure implementations
  * Drawing has its own listener DrawingListener now instead of FigureListener and CompositeFigureListener
  * contains(point, scale) is now called to take view scale into account for finding figures
  * removed DEBUG mode and introduced some logging instead
  * removed DOMStorable from Drawing, Figure
  * introduced a new module **jhotdraw-io** for input output and dom storables
* JDK 17
* maven build process
* restructured project layout
  * introduced submodules

## Quickstart

This projects jars are not yet published to maven central or GitHub packages. To use those you first need to build it with **maven** using: `mv clean install`. Now all jars are published to your local maven repository. And you can include those artifacts using e.g.

```xml
<dependency>
  <groupId>org.jhotdraw</groupId>
  <artifactId>jhotdraw-core</artifactId>
  <version>10.0-SNAPSHOT</version>
</dependency>
```

In module `jhotdraw-samples-mini` are small examples mostly highlighting one aspect of JHotdraw usage.
Additional to that are in module `jhotdraw-samples-misc` more sophisticated examples of using this library.


## License

JHotDraw is licensed under the Lesser General Public License (LGPL) version 2.1 and the Creative Commons Attribution 2.5 License. This means that the library can be used, modified, and distributed freely as long as certain conditions are met, such as giving proper attribution and making any modifications publicly available.

## History 

JHotDraw was initially developed by Erich Gamma in the mid-1990s and has since undergone several updates and revisions. The recent fork of JHotDraw from its original SourceForge repository is intended to continue the development and improvement of the library for future generations of developers.

This is a fork of jhotdraw from http://sourceforge.net/projects/jhotdraw.
