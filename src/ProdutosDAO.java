/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class ProdutosDAO {
    
    private Connection conn;

    // Construtor para inicializar a conexão
    public ProdutosDAO() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost/atv3", "root", "2003");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }

    public void cadastrarProduto(ProdutosDTO produto) {
        String sql = "INSERT INTO leiloes (nome, valor, status) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setInt(2, produto.getValor());
            pstmt.setString(3, produto.getStatus());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar o produto: " + e.getMessage(), e);
        }
    }

    public List<ProdutosDTO> listarProdutos() {
        List<ProdutosDTO> produtos = new ArrayList<>();
        String sql = "SELECT * FROM leiloes";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return produtos;
    }

   public List<ProdutosDTO> listarProdutosVendidos() {
        List<ProdutosDTO> produtosVendidos = new ArrayList<>();
        String sql = "SELECT * FROM leiloes WHERE status = ?";

        // Utiliza try-with-resources para garantir que os recursos sejam fechados corretamente
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/atv3","root","2003");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "Vendido"); // Define o status para "Vendido"

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ProdutosDTO produto = new ProdutosDTO();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setValor(rs.getInt("valor"));
                    produto.setStatus(rs.getString("status"));
                    produtosVendidos.add(produto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log do erro
            // Optionally, you might want to throw a custom exception or handle it in a more specific way
        }

        return produtosVendidos;
    }


    public boolean venderProduto(int id) {
        String sql = "UPDATE leiloes SET status = ? WHERE id = ?";
        boolean sucesso = false;

        // Utiliza try-with-resources para garantir que os recursos sejam fechados corretamente
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/atv3","root","2003");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "Vendido"); // Define o novo status
            pstmt.setInt(2, id); // Define o ID do produto a ser atualizado

            int linhasAfetadas = pstmt.executeUpdate(); // Executa a atualização

            if (linhasAfetadas > 0) {
                sucesso = true; // A atualização foi bem-sucedida
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log do erro
            // Optionally, you might want to throw a custom exception or handle it in a more specific way
        }

        return sucesso; // Retorna true se a atualização foi bem-sucedida, caso contrário, false
    }
}