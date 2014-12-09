
SRC = $(wildcard src/*/*.java)
CLASSES = $(patsubst %.java,%.class,$(patsubst src/%,bin/%,$(SRC)))

t:
	echo $(SRC)
	echo $(CLASSES)

all: $(CLASSES)
	java -cp bin/ jotto.Main

$(CLASSES): $(SRC)
	javac -d bin/ $(SRC)

run: all
