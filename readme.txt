To use the examples, first compile them.  This can be done with the ant command.  This will look something like this:

C:\Users\jeff\workspace\encog-examples>ant
Buildfile: build.xml

init:
    [mkdir] Created dir: C:\Users\jeff\workspace\encog-examples\bin

compile:
    [javac] Compiling 9 source files to C:\Users\jeff\workspace\encog-examples\b
in

dist:
    [mkdir] Created dir: C:\Users\jeff\workspace\encog-examples\lib
      [jar] Building jar: C:\Users\jeff\workspace\encog-examples\lib\encog-examp
les.jar

BUILD SUCCESSFUL
Total time: 2 seconds
C:\Users\jeff\workspace\encog-examples>

Once the examples have been compiled they can be ran with the regular java command.  For example to run the XOR example, use the following command:

java -classpath ./jar/encog-core.jar;./lib/encog-examples.jar org.encog.examples.neural.xorbackprop.XorBackprop

Executing this example will look something like this:

C:\Users\jeff\workspace\encog-examples>java -classpath ./jar/encog-core.jar;./li
b/encog-examples.jar org.encog.examples.neural.xorbackprop.XorBackprop
Epoch #1 Error:0.5557190004909449
Epoch #2 Error:0.5228472979007133
Epoch #3 Error:0.500870951677053
Epoch #4 Error:0.5101135237895661
Epoch #5 Error:0.5302818412204215
Epoch #6 Error:0.5400706376865775
Epoch #7 Error:0.5352510126451284
Epoch #8 Error:0.5196410457205616
Epoch #9 Error:0.5036315507954323
Epoch #10 Error:0.4998444564049617
...
Epoch #4989 Error:0.006084722704485293
Epoch #4990 Error:0.006084077385153464
Epoch #4991 Error:0.006083432267676017
Epoch #4992 Error:0.006082787351948442
Epoch #4993 Error:0.00608214263786603
Epoch #4994 Error:0.006081498125324365
Epoch #4995 Error:0.006080853814219318
Epoch #4996 Error:0.006080209704446444
Epoch #4997 Error:0.0060795657959014145
Epoch #4998 Error:0.006078922088480395
Epoch #4999 Error:0.00607827858207903
Neural Network Results:
0.0,0.0, actual=0.002423368561633513,ideal=0.0
1.0,0.0, actual=0.9941009984156448,ideal=1.0
0.0,1.0, actual=0.9943763950904865,ideal=1.0
1.0,1.0, actual=0.008688268816593979,ideal=0.0

There are other examples that can be run as well.  They will all use a similar command as above.  Additionally, the examples folder is designed to be used as an Eclipse(http://www.eclipse.org) project.  Using an IDE is often the easiest method to execute the examples.