Classes and Jar can be executed from command line (only verified windows 10, unknown if it will work in other terminals).

Compile .class from .java:

	javac *.java

OR

	javac ConnectFourMain.java ConnectFourGameBoardData.java ConnectFourPointer.java ConnectFourHelper.java

Run .class files:

	java ConnectFourMain

Collect .class files into .jar:

	jar cvfme ConnectFourExecuteable.jar MANIFEST.MF ConnectFourMain *.class

OR

	jar cvfme ConnectFourExecuteable.jar MANIFEST.MF ConnectFourMain ConnectFourMain.class ConnectFourGameBoardData.class ConnectFourHelper.class ConnectFourPointer.class

Run .jar:

	java -jar ConnectFourExecuteable.jar