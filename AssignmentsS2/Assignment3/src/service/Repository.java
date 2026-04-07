package AssignmentsS2.Assignment3.src.service;

/*
 * Assignment 3
 * Question: Part 3 - Repository<T>
 * Written by: Catalin-Ion Besleaga (40347936)
 *
 * This generic repository stores SmartTravel entities that are identifiable
 * and naturally comparable. It supports adding items, searching by ID,
 * filtering with Predicate<T>, and returning items sorted by their
 * business-natural ordering.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import AssignmentsS2.Assignment3.src.interfaces.Identifiable;
import AssignmentsS2.Assignment3.src.exceptions.EntityNotFoundException;

public class Repository <T extends Identifiable & Comparable<? super T>> {
    private List<T> items;

    public Repository() {
        items = new ArrayList<>();
    }

    public void add(T item) {if (item != null) items.add(item);}

    public T findById(String id) throws EntityNotFoundException {
        if (id == null || id.trim().isEmpty()) {
            throw new EntityNotFoundException("ID cannot be empty.");
        }

        String trimmed = id.trim();

        for (T element : items)
            if (element != null && trimmed.equalsIgnoreCase(element.getId()))
                return element;

        throw new EntityNotFoundException("Entity with ID '" + trimmed + "' not found.");
    }

    public List<T> filter(Predicate<T> predicate) {
        List<T> result = new ArrayList<>();

        if (predicate == null)
            return result;

        for (T element : items)
            if (element != null && !predicate.test(element))
                result.add(element);

        return result;
    }

    public List<T> getSorted() {
        List<T> currenList = new ArrayList<>(items);
        Collections.sort(currenList);
        return currenList;
    }
}
