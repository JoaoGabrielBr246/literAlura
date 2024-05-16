package br.com.alura.LiterAlura.principal;

import br.com.alura.LiterAlura.model.Autor;
import br.com.alura.LiterAlura.model.DadosLivro;
import br.com.alura.LiterAlura.model.Livro;
import br.com.alura.LiterAlura.repository.AutorRepository;
import br.com.alura.LiterAlura.repository.LivroRepository;
import br.com.alura.LiterAlura.service.ConsumoAPI;
import br.com.alura.LiterAlura.service.ConverteDados;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibirMenu(){
        int op = -1;
        while(op !=0){
            var menu = """
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    0 - Sair
                    """;
            System.out.println(menu);
            op = sc.nextInt();
            sc.nextLine();

            switch (op){
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosEmDeterminadoIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void buscarLivro() {
        System.out.print("Digite o título do livro para buscar: ");
        String tituloLivro = sc.nextLine();
        String url = ENDERECO + tituloLivro.replace(" ","%20");

        String dadosLivroJson = consumo.obterDados(url);
        System.out.println("JSON retornado pela busca:");
        System.out.println(dadosLivroJson);
        List<DadosLivro> dadosLivros = conversor.obterDados(dadosLivroJson);
        if (!dadosLivros.isEmpty()) {
            DadosLivro primeiroLivro = dadosLivros.get(0);
            Livro livro = new Livro(primeiroLivro);
            livroRepository.save(livro);

            System.out.println("Livro encontrado:");
            System.out.println("Título: " + primeiroLivro.titulo());
            System.out.println("Autores: " + primeiroLivro.autor());
            System.out.println("Número de Downloads: " + primeiroLivro.numeroDownloads());
        } else {
            System.out.println("Nenhum livro encontrado com esse título.");
        }
    }

    @Transactional
    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (!livros.isEmpty()) {
            System.out.println("Livros registrados:");
            int i = 1;
            for (Livro livro : livros) {
                System.out.println("Registro #"+i);
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Número de Downloads: " + livro.getNumeroDownloads());
                System.out.println("Autor(a): ");
                livro.getAutores().forEach(autor -> System.out.println("- " + autor.getNome()));
                System.out.println("---------------------------------------\n");
                i++;
            }
        } else {
            System.out.println("Não há livros registrados.");
        }
    }

    private void listarAutores() {
        List<Livro> livros = livroRepository.findAll();
        if (!livros.isEmpty()) {
            System.out.println("Livros registrados:");
            int i = 1;
            for (Livro livro : livros) {
                System.out.println("Autores do livro #" + i + ":");
                List<Autor> autores = livro.getAutores();
                for (Autor autor : autores) {
                    System.out.println("- Nome: " + autor.getNome());
                    System.out.println("  Data de Nascimento: " + autor.getBirth_year());
                    System.out.println("  Data de Morte: " + autor.getDeath_year());
                }
                System.out.println("---------------------------------------\n");
                i++;
            }
        } else {
            System.out.println("Não há livros registrados.");
        }
    }


    private void listarAutoresVivos() {
        System.out.print("Digite o ano para buscar autores vivos: ");
        int ano = sc.nextInt();
        List<Autor> autoresVivos = autorRepository.buscarAutoresVivosNoAno(ano);

        if (!autoresVivos.isEmpty()) {
            System.out.println("Autores vivos em " + ano + ":");
            autoresVivos.forEach(autor -> System.out.println("- " + autor.getNome()));
        } else {
            System.out.println("Não há autores vivos em " + ano + ".");
        }
    }

    private void listarLivrosEmDeterminadoIdioma() {
        System.out.print("Digite o idioma para buscar os livros: ");
        String idioma = sc.nextLine();
        List<Livro> livrosPorIdioma = livroRepository.buscarLivrosPorIdioma(idioma);

        if (!livrosPorIdioma.isEmpty()) {
            System.out.println("Livros em " + idioma + ":");
            int i = 1;
            for (Livro livro : livrosPorIdioma) {
                System.out.println("Livro #" + i);
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Número de Downloads: " + livro.getNumeroDownloads());
                System.out.println("---------------------------------------\n");
                i++;
            }
        } else {
            System.out.println("Não há livros em " + idioma + ".");
        }
    }
}
