package pl.casmic.fileuploader.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.casmic.fileuploader.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, String> {
}
