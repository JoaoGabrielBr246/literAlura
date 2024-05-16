package br.com.alura.LiterAlura.service;

import br.com.alura.LiterAlura.model.DadosLivro;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ConverteDados {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<DadosLivro> obterDados(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode resultsNode = rootNode.get("results");
            return mapper.readValue(resultsNode.toString(), new TypeReference<List<DadosLivro>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
