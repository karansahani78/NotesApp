package com.karan.Note.repository;

import com.karan.Note.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    public Optional<Note> findByTitle(String title);

}
