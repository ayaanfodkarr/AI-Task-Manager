package com.example.aitaskmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private EditText etTaskTitle;
    private EditText etTaskDescription;
    private Button btnAddTask;
    private Button btnAnalyzeAll;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private final List<Task> taskList = new ArrayList<>();
    private TaskAdapter adapter;
    private GeminiHelper geminiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geminiHelper = new GeminiHelper();

        bindViews();
        setupRecyclerView();
        setupListeners();
    }

    private void bindViews() {
        etTaskTitle       = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        btnAddTask        = findViewById(R.id.btnAddTask);
        btnAnalyzeAll     = findViewById(R.id.btnAnalyzeAll);
        recyclerView      = findViewById(R.id.recyclerView);
        progressBar       = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new TaskAdapter(taskList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddTask.setOnClickListener(v -> addTask());

        btnAnalyzeAll.setOnClickListener(v -> {
            if (taskList.isEmpty()) {
                Toast.makeText(this, "Add tasks first!", Toast.LENGTH_SHORT).show();
                return;
            }
            analyzeTaskAt(0); // Kick off sequential analysis starting from index 0
        });
    }

    private void addTask() {
        String title = etTaskTitle.getText().toString().trim();
        String description = etTaskDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etTaskTitle.setError("Task title is required");
            etTaskTitle.requestFocus();
            return;
        }

        Task task = new Task(title, description);
        taskList.add(0, task); // Add to top
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);

        etTaskTitle.setText("");
        etTaskDescription.setText("");
    }

    // Called from the per-item analyse button
    @Override
    public void onAnalyze(int position) {
        analyzeTaskAt(position);
    }

    @Override
    public void onDelete(int position) {
        taskList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, taskList.size());
    }

    @Override
    public void onStatusCycle(int position) {
        Task task = taskList.get(position);
        switch (task.getStatus()) {
            case PENDING:
                task.setStatus(Task.Status.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                task.setStatus(Task.Status.DONE);
                break;
            case DONE:
                task.setStatus(Task.Status.PENDING);
                break;
        }
        adapter.notifyItemChanged(position);
        Toast.makeText(this, "Status: " + task.getStatusLabel(), Toast.LENGTH_SHORT).show();
    }

    private void analyzeTaskAt(int position) {
        if (position >= taskList.size()) {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "AI analysis complete!", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        Task task = taskList.get(position);

        runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            btnAnalyzeAll.setEnabled(false);
            Toast.makeText(this,
                    "Analyzing: " + task.getTitle(), Toast.LENGTH_SHORT).show();
        });

        geminiHelper.analyzeTask(task, new GeminiHelper.GeminiCallback() {
            @Override
            public void onSuccess(String result) {
                task.setAiAnalysis(result);
                task.setPriority(GeminiHelper.extractPriority(result));

                runOnUiThread(() -> {
                    adapter.notifyItemChanged(position);
                    progressBar.setVisibility(View.GONE);
                    btnAnalyzeAll.setEnabled(true);
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnAnalyzeAll.setEnabled(true);
                    Toast.makeText(MainActivity.this,
                            "Analysis failed: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
