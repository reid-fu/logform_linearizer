from nltk.translate.bleu_score import sentence_bleu, SmoothingFunction
import sys

ref_sent = sys.argv[1]
test_sent = sys.argv[2]
ref = [ref_sent.split(' ')]
test = test_sent.split(' ')
score = sentence_bleu(ref, test, smoothing_function=SmoothingFunction().method1)
print(score)