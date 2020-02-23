package com.example.recognizetext.customview;



import com.example.recognizetext.tflite.Classifier;

import java.util.List;

public interface ResultsView {
    public void setResults(final List<Classifier.Recognition> results);
}
