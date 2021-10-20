import React from "react";
import Head from "next/head";
import { AdminLayout } from "../../layouts/Admin";
import { UserLayout } from "../../layouts/User";


const Dashboard = () => {
  return (
    <>
      <Head>
        <title>Dashboard</title>
      </Head>
      <h1 className="text-center font-black tracking-tight text-6xl">
        Hello dashboard page
      </h1>
    </>
  );
};
Dashboard.getLayout = function getLayout(page) {
  return <UserLayout>{page}</UserLayout>;
};

export default Dashboard;
