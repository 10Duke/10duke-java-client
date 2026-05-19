# 10Duke examples for Java [END-OF-LIFE]

> **IMPORTANT NOTICE:** This repository and the Java client libraries within
it are **no longer maintained**. This project has reached its
End-of-Life (EOL). 

For the latest updates, active development, and modern Java SDKs, please
transition to our new codebase hosted on GitLab.

---

## 📦 Relocation Information

The 10Duke Java SDKs and examples have moved to GitLab:
👉 **[https://gitlab.com/10Duke](https://gitlab.com/10Duke)**

The new architecture is split into a **Core** library and product-specific
licensing libraries:

1. **10Duke Core** Contains shared utilities for implementing OAuth 2.0
and OpenID Connect (OIDC) flows.  

   🔗 [Core GitLab Repository](https://gitlab.com/10Duke/core/java-core)

2. **10Duke Scale** The official Java client library for 10Duke Scale.  

   🔗 [Scale Java GitLab Repository](https://gitlab.com/10Duke/scale/java)

3. **10Duke Enterprise** *Note: A dedicated licensing client library has
   not yet been implemented for Java.*

---

## Legacy Repository Information (Archived)

*The information below applies only to this deprecated codebase and is kept for historical reference.*

This repository contains legacy 10Duke client libraries for Java, split into multiple artifacts to minimize dependencies. 

* **Requirements:** Targets JDK 11 (without modules).
* **Legacy Modules:** * [Single Sign-On for JavaFX](./sso/javafx/) ([Demo](./sso/javafx-demo))
