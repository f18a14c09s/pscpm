# PKI-Enabled Smart Card Password Manager (PSCPM) - Overview

The PSCPM client is a Java-based, desktop password/credential manager that protects the sensitive information using
 symmetric cryptography and protects the symmetric keys using PKI-enabled smart card (two-factor, asymmetric) cryptography.

PSCPM has a server-side component, to which the client component can delegate, for persisting the user's data online.
Alternatively, the client application can work offline and store the data in the user's computer.

The PSCPM client currently supports Windows only due to the use of Microsoft's cryptographic API (CAPI) for smart card cryptography.
