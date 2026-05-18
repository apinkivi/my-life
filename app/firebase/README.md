# My Life - Firebase & Graph API Integration

This directory contains the Firebase project (including Cloud Functions) for the personal AI assistant.

## Microsoft Entra ID (Graph API) Setup

To sync tasks from Microsoft To Do, you need to register an application in your organization's Entra ID (formerly Azure AD). We use the name **"Productivity Sync"** to be inconspicuous.

### 1. Register the Application

1. Go to [Entra ID admin center](https://entra.microsoft.com/) -> **Applications** -> **App registrations**.
2. Click **New registration**.
3. **Name:** `Productivity Sync`
4. **Supported account types:** _Accounts in this organizational directory only (Single tenant)_.
5. **Redirect URI:** Leave empty for now.

### 2. Enable Public Client Flows (For Device Code Auth)

To test the API easily from the command line, we use the Device Code flow.

1. In your app's menu, go to **Authentication**.
2. Scroll down to **Advanced settings** -> **Allow public client flows**.
3. Set it to **Yes** and save.

### 3. Add API Permissions

1. Go to **API permissions**.
2. Click **Add a permission** -> **Microsoft Graph** -> **Delegated permissions**.
3. Search for and select:
   - `Tasks.ReadWrite` (Allows reading and modifying your To Do tasks).
   - `User.Read` (Usually there by default).
4. Click **Add permissions**.

### 4. Gather Credentials

From the app's **Overview** page, copy the following values:

- **Application (client) ID**
- **Directory (tenant) ID**

### 5. Configure Environment Variables

Create a file named `.env` in the `app/firebase/functions/` directory and add your IDs:

```env
MICROSOFT_TENANT_ID=your_tenant_id_here
MICROSOFT_CLIENT_ID=your_client_id_here
```

_(Note: We don't need a Client Secret when using Delegated Permissions via Device Code flow. The script will ask you to log in as yourself)._

## Testing the Connection

Go to the `functions` directory and install the necessary dependencies:

```bash
cd app/firebase/functions
npm install @azure/identity @microsoft/microsoft-graph-client isomorphic-fetch
npm install -D dotenv ts-node
```

Run the test script to verify your connection and permissions:

```bash
npx ts-node src/testGraphApi.ts
```

The script will output a code and a URL. Open the URL in your browser, enter the code, and log in. Once authenticated, the script will fetch and display your task lists.

## Links

- [Microsoft Graph REST API v1.0: todoTask resource type](https://learn.microsoft.com/en-us/graph/api/resources/todotask?view=graph-rest-1.0)
