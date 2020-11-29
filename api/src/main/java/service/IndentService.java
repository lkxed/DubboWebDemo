package service;

import model.Indent;

import java.util.List;

/**
 * @author lkxed
 */
public interface IndentService {
    Indent query(int id);

    List<Indent> queryAll();

    Indent create(String name, String username, int number);
}
