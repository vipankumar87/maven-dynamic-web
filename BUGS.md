# Bug Log — JDBC Implementation Fix

## Error Reported

```
org.apache.jasper.JasperException: Unable to get JAR resource [/jakarta.tags.core]
containing TLD: [java.io.FileNotFoundException]
```

---

## Root Cause Analysis

### Bug 1 — Maven JARs not deployed to `WEB-INF/lib` (primary cause)

**File:** `.classpath`

The Eclipse WTP deployment mechanism requires the Maven classpath container entry to carry the attribute:

```xml
<attribute name="org.eclipse.jst.component.dependency" value="/WEB-INF/lib"/>
```

Without it, Eclipse WTP treats Maven dependencies as compile-time only and **never copies them into the deployed webapp's `WEB-INF/lib`**. As a result:

- `org.glassfish.web:jakarta.servlet.jsp.jstl` (JSTL 3.0.1) — **not deployed**
  → Tomcat Jasper cannot find the TLD for `jakarta.tags.core` → `JasperException`
- `com.mysql:mysql-connector-j` (9.1.0) — **not deployed**
  → `DBConnection.getConnection()` throws `ClassNotFoundException` for the MySQL driver

**Fix applied:**

```xml
<!-- Before -->
<classpathentry kind="con" path="org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER">
    <attributes>
        <attribute name="maven.pomderived" value="true"/>
    </attributes>
</classpathentry>

<!-- After -->
<classpathentry kind="con" path="org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER">
    <attributes>
        <attribute name="maven.pomderived" value="true"/>
        <attribute name="org.eclipse.jst.component.dependency" value="/WEB-INF/lib"/>
    </attributes>
</classpathentry>
```

---

### Bug 2 — JRE container pointed to Java 8 instead of Java 17

**File:** `.classpath`

The JRE container entry referenced `JavaSE-1.8`, while `pom.xml` sets `maven.compiler.release=17`. This mismatch causes Eclipse to compile Java 17 source against a Java 8 JRE, leading to potential `UnsupportedClassVersionError` at runtime.

**Fix applied:**

```xml
<!-- Before -->
<classpathentry kind="con"
    path="org.eclipse.jdt.launching.JRE_CONTAINER/.../JavaSE-1.8">

<!-- After -->
<classpathentry kind="con"
    path="org.eclipse.jdt.launching.JRE_CONTAINER/.../JavaSE-17">
```

---

## Steps to Apply in Eclipse

After pulling this commit:

1. Right-click the project → **Maven → Update Project** (`Alt+F5`) — Eclipse re-reads `.classpath`.
2. Verify **Project Properties → Java Build Path → Libraries** shows `JavaSE-17`.
3. Under the **Deployment Assembly** tab, confirm `Maven Dependencies → /WEB-INF/lib` is listed.
4. Restart the Tomcat 11 server — the JSTL and MySQL JARs are now deployed.

---

## Verification Checklist

- [ ] Login page loads without `JasperException`
- [ ] Registration saves a user row to MySQL `users` table
- [ ] Login authenticates against DB (not in-memory store)
- [ ] Admin dashboard shows correct DB-backed user counts

---

## Bug 3 — Tables not auto-created on startup

**Reported:** `webblog` database tables are not auto-created; app crashes on first run because the `users` table does not exist.

**Root cause:** There was no mechanism to run `schema.sql` at startup. The file existed only as a manual reference.

**Fix applied:** Added `SchemaInitializer.java` — a `@WebListener` that runs `contextInitialized()` once when Tomcat deploys the app.

**File:** `src/main/java/com/rudracomputer/webblog/db/SchemaInitializer.java`

What it does, in order:
1. Connects to MySQL **without** a database selected (`localhost:3306/`) and runs `CREATE DATABASE IF NOT EXISTS webblog`.
2. Connects via `DBConnection` and runs `CREATE TABLE IF NOT EXISTS users (...)`.
3. Inserts a default admin via `INSERT IGNORE` (safe to re-run — no duplicate rows):
   - **Email:** `admin@webblog.com`
   - **Password:** `Admin@123`
   - Hash is generated at runtime by `PasswordUtils.hash()` so it always matches the login check.

No manual SQL execution is needed. A clean server start is enough.
