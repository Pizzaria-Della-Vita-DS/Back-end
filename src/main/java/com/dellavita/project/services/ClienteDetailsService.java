package com.dellavita.project.services;

import com.dellavita.project.repositories.ClienteRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class ClienteDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    public ClienteDetailsService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) clienteRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado: " + email));
    }
}