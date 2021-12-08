package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

public class JSONService<T> {

    public List<T> convert(String resourceFileName) throws IOException {

        try (Reader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(this.getClass().getResourceAsStream("/" + resourceFileName))))) {

            return new Gson()
                    .fromJson(reader,
                            new TypeToken<List<T>>() {
                            }.getType());

        }
    }
}
