package com.karan.Note.controller;

import com.karan.Note.model.Note;
import com.karan.Note.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/save")
    public ResponseEntity<Note> save(@RequestBody Note note) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.saveNote(note));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(noteService.findById(id));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Note> findByTitle(@PathVariable String title) {
        return ResponseEntity.status(HttpStatus.FOUND).body(noteService.findByTitle(title));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteService.findAll());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Note note) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(id, note));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}