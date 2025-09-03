package com.karan.Note.service;

import com.karan.Note.exceptions.NoteNotFoundException;
import com.karan.Note.model.Note;
import com.karan.Note.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found for id: " + id));
    }

    public Note findByTitle(String title) {
        return noteRepository.findByTitle(title)
                .orElseThrow(() -> new NoteNotFoundException("Note not found for title: " + title));
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note updateNote(Long id, Note note) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found for id: " + id));

        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());
        // no need to set createdAt or updatedAt manually
        return noteRepository.save(existingNote);
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException("Note not found for id: " + id);
        }
        noteRepository.deleteById(id);
    }
}
