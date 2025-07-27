package com.giggi.basesetup.mapper;

import com.giggi.basesetup.dto.request.auth.RegisterRequest;
import com.giggi.basesetup.entity.Role;
import com.giggi.basesetup.entity.RoleName;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.giggi.basesetup.entity.Utente;
import com.giggi.basesetup.dto.request.utente.UtenteCreateRequestDTO;
import com.giggi.basesetup.dto.request.utente.UtenteUpdateRequestDTO;
import com.giggi.basesetup.dto.response.utente.UtenteFindDTO;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtenteMapper {

    Utente convert(UtenteCreateRequestDTO dto);

    Utente convert(UtenteUpdateRequestDTO dto);

    Utente convert(RegisterRequest dto);

    Utente convert(UtenteFindDTO dto);

    UtenteFindDTO conver(Utente entity);

    List<UtenteFindDTO> convert(List<Utente> entities);

    // Metodo di mapping personalizzato
    default Set<Role> map(Set<String> value) {
        if (value == null) return null;
        return value.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toSet());
    }

}