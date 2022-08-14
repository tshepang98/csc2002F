JAVAC=/usr/bin/javac
SRCDIR=src
BINDIR=bin
docdir=./docs

default:
	$(JAVAC) -d $(BINDIR) $(SRCDIR)/*.java
clean:
	rm $(BINDIR)/*.class
parallelMedian:
	java -cp bin MedianFilerParallel
Sequential:
	java -cp bin SequentialFilter
correctness:
	java -cp bin Correctness
experiment:
	java -cp bin Experiment
docs:
	javadoc -d $(docdir) $(SRCDIR)/*.java

