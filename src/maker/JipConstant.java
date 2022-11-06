package maker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JipConstant {
    public static final String[] fileNames = {
            "/controller",
            "/dto",
            "/dto/request",
            "/dto/response",
            "/dto/search",
            "/repository",
            "/service"
//            "/service/impl"
    };
    public static final String[] packageName = {
            ".controller",
            ".dto.request",
            ".dto.response",
            ".repository",
            ".service"
//            "/service/impl"
    };

    public static final String[] importLombokAndOthers = {
            "import lombok.AllArgsConstructor;",
            "import lombok.Builder;",
            "import lombok.Data;",
            "import lombok.NoArgsConstructor;"
    };

    public static final String[] annotationName = {
            "@AllArgsConstructor\n",
            "@NoArgsConstructor\n",
            "@Data\n",
            "@Builder\n"
    };

    public static final String[] GenericImportService = {
            "import com.us_bangla.fair_comparison.common.generic.service.AbstractSearchService;",
            "import org.springframework.data.jpa.domain.Specification;",
            "import org.springframework.stereotype.Service;",
            "import com.us_bangla.fair_comparison.common.generic.specification.CustomSpecification;"

    };
    public static final String[] GenericImportController = {
            "import com.us_bangla.fair_comparison.common.generic.controller.AbstractSearchController;",
            "import org.springframework.web.bind.annotation.RequestMapping;",
            "import org.springframework.web.bind.annotation.RestController;"

    };

    public static final String IdQ = "import com.us_bangla.fair_comparison.common.generic.payload.seatch.IdQuerySearchDto;";

    public static final String repositoryImport =
            "import org.springframework.stereotype.Repository;";

    public static final String IDto = "import com.us_bangla.fair_comparison.common.generic.payload.marker.IDto;";
    public static final List<String> otherImport = new ArrayList<>();

    public static final Map<String, String> mvcDirectoryPath = new HashMap<>();
    public static final Map<String, String> packageImport = new HashMap<>();
    public static final String abstractRepoImport = "import com.us_bangla.fair_comparison.common.generic.repository.AbstractRepository;";
    public static final String abstractIQDto = "import com.us_bangla.fair_comparison.common.generic.payload.seatch.IdQuerySearchDto;";
}
