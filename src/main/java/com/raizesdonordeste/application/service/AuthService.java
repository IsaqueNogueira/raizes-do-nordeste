package com.raizesdonordeste.application.service;

import com.raizesdonordeste.application.dto.AuthResponse;
import com.raizesdonordeste.application.dto.LoginRequest;
import com.raizesdonordeste.application.dto.RegisterRequest;
import com.raizesdonordeste.domain.model.Fidelidade;
import com.raizesdonordeste.domain.model.Usuario;
import com.raizesdonordeste.domain.repository.FidelidadeRepository;
import com.raizesdonordeste.domain.repository.UsuarioRepository;
import com.raizesdonordeste.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FidelidadeRepository fidelidadeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        if (!usuario.isAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getPerfil().name());

        return new AuthResponse(
                token,
                "Bearer",
                86400000L,
                new AuthResponse.UsuarioInfo(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getPerfil().name()
                )
        );
    }

    public Usuario registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (!request.isConsentimentoLGPD()) {
            throw new RuntimeException("Consentimento LGPD é obrigatório");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setPerfil(request.getPerfil());
        usuario.setConsentimentoLGPD(true);
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);

        if (request.getPerfil() == Usuario.PerfilUsuario.CLIENTE) {
            Fidelidade fidelidade = new Fidelidade();
            fidelidade.setClienteId(salvo.getId());
            fidelidade.setPontos(0);
            fidelidade.setHistorico(new ArrayList<>());
            fidelidadeRepository.save(fidelidade);
        }

        return salvo;
    }
}