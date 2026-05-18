import { DeviceCodeCredential } from "@azure/identity";
import { Client } from "@microsoft/microsoft-graph-client";
import { TokenCredentialAuthenticationProvider } from "@microsoft/microsoft-graph-client/authProviders/azureTokenCredentials";
import * as dotenv from "dotenv";
import "isomorphic-fetch";

// Load environment variables from .env file
dotenv.config();

const tenantId = process.env.MICROSOFT_TENANT_ID;
const clientId = process.env.MICROSOFT_CLIENT_ID;

if (!tenantId || !clientId) {
    console.error("Missing MICROSOFT_TENANT_ID or MICROSOFT_CLIENT_ID in .env file.");
    process.exit(1);
}

async function testConnection() {
    console.log("Initializing Graph API connection...");

    // DeviceCodeCredential is great for CLI tools. It will prompt you to visit a URL and enter a code.
    const credential = new DeviceCodeCredential({
        tenantId: tenantId,
        clientId: clientId,
        userPromptCallback: (info) => {
            console.log("\n=======================================================");
            console.log(info.message);
            console.log("=======================================================\n");
        },
    });

    // Create an authentication provider utilizing the DeviceCodeCredential
    const authProvider = new TokenCredentialAuthenticationProvider(credential, {
        scopes: ["Tasks.ReadWrite", "User.Read"],
    });

    // Initialize the Graph client
    const graphClient = Client.initWithMiddleware({
        authProvider: authProvider,
    });

    try {
        console.log("Requesting access (you may need to authenticate)...");
        
        // Fetch the user's profile to verify basic access
        const me = await graphClient.api("/me").get();
        console.log(`\nSuccessfully authenticated as: ${me.displayName} (${me.userPrincipalName})`);

        console.log("\nFetching your Microsoft To Do Task Lists...");
        // Fetch the task lists
        const taskLists = await graphClient.api("/me/todo/lists").get();
        
        if (taskLists.value && taskLists.value.length > 0) {
            console.log(`Found ${taskLists.value.length} task lists:`);
            taskLists.value.forEach((list: any) => {
                console.log(` - ${list.displayName} (ID: ${list.id})`);
            });
            console.log("\n✅ Connection test successful!");
        } else {
            console.log("No task lists found, but connection was successful.");
        }

    } catch (error) {
        console.error("\n❌ Error connecting to Graph API:");
        console.error(error);
    }
}

testConnection();
