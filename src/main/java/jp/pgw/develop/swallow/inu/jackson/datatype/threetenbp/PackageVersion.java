package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;

public final class PackageVersion implements Versioned {

    public final static Version VERSION = VersionUtil.parseVersion(
        "2.6.0", "com.fasterxml.jackson.datatype", "jackson-datatype-threetenbp"
    );

    @Override
    public Version version() {
        return VERSION;
    }

}
