all: BinarieReader.png BinarieWriter.png
clean:
	rm Binarie*.png
BinarieReader.png: BinarieReader.dot
	dot -Tpng $< -o $@
BinarieWriter.png: BinarieWriter.dot
	dot -Tpng $< -o $@
