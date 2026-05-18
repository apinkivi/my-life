import { DeviceCodeCredential } from "@azure/identity";
import { Client } from "@microsoft/microsoft-graph-client";
import { TokenCredentialAuthenticationProvider } from "@microsoft/microsoft-graph-client/authProviders/azureTokenCredentials";
import * as dotenv from "dotenv";
import * as fs from "fs";
import * as path from "path";
import "isomorphic-fetch";

dotenv.config();

const tenantId = process.env.MICROSOFT_TENANT_ID;
const clientId = process.env.MICROSOFT_CLIENT_ID;

if (!tenantId || !clientId) {
    console.error("Missing MICROSOFT_TENANT_ID or MICROSOFT_CLIENT_ID in .env file.");
    process.exit(1);
}

// Luo varmuuskopiokansion rakenteen: c:\github\my-life\backup\todo\yyyy-MM-dd
function getBackupDir(): string {
    const date = new Date();
    const yyyy = date.getFullYear();
    const MM = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    const dateStr = `${yyyy}-${MM}-${dd}`;
    
    // __dirname on tässä 'app/firebase/functions/src'
    // Neljä tasoa ylöspäin on 'my-life' -juurikansio
    const backupDir = path.join(__dirname, "..", "..", "..", "..", "backup", "todo", dateStr);
    
    if (!fs.existsSync(backupDir)) {
        fs.mkdirSync(backupDir, { recursive: true });
    }
    return backupDir;
}

async function runBackup() {
    console.log("Initializing Graph API connection for backup...");

    const credential = new DeviceCodeCredential({
        tenantId: tenantId,
        clientId: clientId,
        userPromptCallback: (info) => {
            console.log("\n=======================================================");
            console.log(info.message);
            console.log("=======================================================\n");
        },
    });

    const authProvider = new TokenCredentialAuthenticationProvider(credential, {
        scopes: ["Tasks.ReadWrite", "User.Read"],
    });

    const graphClient = Client.initWithMiddleware({
        authProvider: authProvider,
    });

    try {
        const backupDir = getBackupDir();
        console.log(`\nBackup directory: ${backupDir}\n`);

        console.log("Fetching Task Lists...");
        const listsResponse = await graphClient.api("/me/todo/lists").get();
        const lists = listsResponse.value;

        // Tallennetaan itse listojen tiedot
        fs.writeFileSync(
            path.join(backupDir, "lists.json"), 
            JSON.stringify(lists, null, 2)
        );
        console.log(`✅ Saved ${lists.length} lists metadata to lists.json`);

        // Käydään läpi jokainen lista ja haetaan sen kaikki tehtävät
        for (const list of lists) {
            console.log(`\nFetching tasks for list: ${list.displayName}...`);
            const allTasks: any[] = [];
            
            // Haetaan kaikki tehtävät. Graph API palauttaa vain tietyn määrän kerrallaan,
            // joten meidän täytyy seurata @odata.nextLink -URLia kunnes kaikki on haettu.
            let nextLink: string | null = `/me/todo/lists/${list.id}/tasks`;
            
            while (nextLink) {
                const tasksResponse = await graphClient.api(nextLink).get();
                
                if (tasksResponse.value && tasksResponse.value.length > 0) {
                    allTasks.push(...tasksResponse.value);
                }
                
                // Jos on lisää sivuja, asetetaan seuraava URL. Muuten null, mikä katkaisee loopin.
                if (tasksResponse["@odata.nextLink"]) {
                    nextLink = tasksResponse["@odata.nextLink"];
                } else {
                    nextLink = null;
                }
            }

            // Tehdään tiedostonimestä turvallinen (poistetaan erikoismerkit)
            const safeName = list.displayName.replace(/[^a-z0-9]/gi, '_').toLowerCase();
            const filePath = path.join(backupDir, `tasks_${safeName}.json`);
            
            fs.writeFileSync(filePath, JSON.stringify(allTasks, null, 2));
            console.log(`✅ Saved ${allTasks.length} tasks to tasks_${safeName}.json`);
        }

        console.log("\n🎉 Backup completed successfully!");

    } catch (error) {
        console.error("\n❌ Error during backup:");
        console.error(error);
    }
}

runBackup();
