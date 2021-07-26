package procceed.sw.iam.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import procceed.sw.iam.authservice.entities.Client;
import procceed.sw.iam.authservice.services.ClientService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity createClient(UriComponentsBuilder uriComponentsBuilder,
                                       @Valid @RequestBody Client client) {
        clientService.createClient(client);

        UriComponents uriComponents = uriComponentsBuilder.path("/clients/" + client.getId()).build();

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = (clientService.getAllClients());

        return ResponseEntity.ok(clients);
    }
}
